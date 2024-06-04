import React, {useContext, useEffect, useState} from 'react';
import ValueContext from '../ValueContext';
import {useNavigate} from 'react-router-dom';
import styles from './MainMenu.module.css';
import stylesLogin from './LoginForm.module.css';
import stylesSignup from './SignupForm.module.css';
import Background from '../authentication/Background';
import axios from "axios";

const MainMenu = ({onPlayer1NicknameChange}) => {
    const navigate = useNavigate();
    const {
        player1Nickname, setPlayer1Nickname,
        player2Nickname, setPlayer2Nickname
    } = useContext(ValueContext);
    const [isNicknameAvailable, setIsNicknameAvailable] = useState(null);
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isRegistered, setIsRegistered] = useState(false);
    const [loginError, setLoginError] = useState('');
    const [showLogin, setShowLogin] = useState(true);
    const [showSignup, setShowSignup] = useState(false);
    const [currentPlayer, setCurrentPlayer] = useState(1);
    const [showMenu, setShowMenu] = useState(true);


    const clearFormData = () => {
        setNickname('');
        setPassword('');
        setIsNicknameAvailable(null);
    };


    useEffect(() => {
        let errorTimeout;
        if (loginError) {
            errorTimeout = setTimeout(() => {
                setLoginError('');
            }, 3000);
        }
        return () => clearTimeout(errorTimeout);
    }, [loginError]);

    const handlePlayerLoginSuccess = (nickname) => {
        if (currentPlayer === 1) {
            setPlayer1Nickname(nickname);
            setCurrentPlayer(2);
            clearFormData();
            setShowSignup(false);
            setShowLogin(true);
        } else {
            if (player1Nickname !== nickname) {
                setPlayer2Nickname(nickname);
                setIsLoggedIn(true);
                setShowLogin(false);
                setShowSignup(false);
            } else {
                setLoginError('Cannot enter the same account twice.');
                clearFormData();
            }
        }
    };

    const handlePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        const userExists = await checkForExistingPlayer(nickname);
        if (userExists) {
            const loggedIn = await handleLogin(nickname, password);
            if (loggedIn) {
                handlePlayerLoginSuccess(nickname);
                clearFormData();
            } else {
                setLoginError('Incorrect password. Please try again.');
                clearFormData();
            }
        } else {
            setLoginError('No such user found. Would you like to sign up?');
            clearFormData();
        }
    };

    const handleLogin = async (nickname, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/player/checkers/log-in', {
                method: 'POST', headers: {
                    'Content-Type': 'application/json',
                }, body: JSON.stringify({nickname, password}),
            });

            if (response.ok && response.status !== 204 && response.headers.get('content-length') !== '0' && response.status !== 401) {
                const playerData = await response.json();
                console.log(playerData);
                clearFormData();
                return true;
            } else {
                if (response.headers.get('content-length') === '0' || response.status === 401) {
                    setLoginError('Incorrect password. Please try again.');
                    console.log('Empty JSON or 401 response status');
                    clearFormData();
                } else {
                    console.log('Some shit have happened');
                    setLoginError('An error occurred. Please try again later.');
                    clearFormData();
                }
                return false;
            }
        } catch (error) {
            console.error('There was an error during login:', error);
            setLoginError('Incorrect password. Please try again.');
            clearFormData();
            return false;
        }
    };

    const checkNickname = async (nickname) => {
        try {
            const response = await fetch(`http://localhost:8080/api/player/checkers/exists?nickname=${nickname}`);
            if (response.ok) {
                const result = await response.text();
                console.log('Response:', result);
                return result === "true";
            } else {
                console.error('Request failed:', response.status);
                return false;
            }
        } catch (error) {
            console.error('There was an error:', error);
            return false;
        }
    };

    const signUp = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/player/checkers/sign-up', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({nickname, password})
            });
            if (response.ok) {
                const playerData = await response.json();
                setIsRegistered(true);
                clearFormData();
                return true;
            } else {
                console.error('Registration failed:', response.status);
                setIsRegistered(false);
                clearFormData();
                return false;
            }
        } catch (error) {
            console.error('There was an error during registration:', error);
            setIsRegistered(false);
            clearFormData();
            return false;
        }
    };

    const handleSignupSubmit = async (e) => {
        e.preventDefault();
        if (isNicknameAvailable) {
            const signUpResult = await signUp();
            if (signUpResult) {
                handlePlayerLoginSuccess(nickname);
                clearFormData();
            } else {
                setLoginError('Registration failed. Please try again.');
                clearFormData();
            }
        }
    };

    const checkForExistingPlayer = async (nickname) => {
        try {
            const response = await fetch(`http://localhost:8080/api/player/checkers/exists?nickname=${nickname}`);
            if (response.ok) {
                const result = await response.text();
                return result === 'true';
            } else {
                navigate('/signup');
            }
        } catch (error) {
            console.error('There was an error:', error);
        }
    };

    const handleNicknameChange = async (e) => {
        const newNickname = e.target.value;
        setNickname(newNickname);
        if (newNickname) {
            const exists = await checkNickname(newNickname);
            setIsNicknameAvailable(!exists);
            setLoginError('No such user found. Would you like to sign up?');
        } else {
            setIsNicknameAvailable(null);
        }
    };

    const resetPlayers = async () => {
        try {
            const response = await fetch(`http://localhost:8080/game/reset-players?player1Name=${player1Nickname}&player2Name=${player2Nickname}`, {
                method: 'POST'
            });
            if (response.ok) {
                setPlayer1Nickname('');
                setPlayer2Nickname('');
                console.log('Players have been reset successfully');
                navigate('/menu');
            } else {
                console.error('Failed to reset players:', response.status);
            }
        } catch (error) {
            console.error('Error resetting players:', error);
        }
    };

    const startGame = async () => {
        console.log('Attempting to start game with Player 1:', player1Nickname, 'and Player 2:', player2Nickname);
        try {
            const response = await axios.post('http://localhost:8080/game/start', null, {
                params: {
                    player1Name: player1Nickname,
                    player2Name: player2Nickname
                }
            });
            console.log('Game started:', response.data);
            navigate("/game");
        } catch (error) {
            console.error('Error starting game:', error);
        }
    };

    const handlePlayButtonClick = () => {
        startGame();
    };

    const handleBackClick = () => {
        resetPlayers();
    };

    return (
        <Background>
            <div className={styles.container}>
                {/*<div className={styles.menuCard}>*/}
                <h1 className={`${styles.title} ${styles.floatingText}`}>Checkers Game</h1>
                {/*{loginError && <p className={`${stylesLogin.error} ${loginError && 'show'}`}>{loginError}</p>}*/}
                {player1Nickname && currentPlayer === 2 &&
                    <div className={`${styles.welcomeMessage}`}>
                        <h2>Welcome home, {player1Nickname}!</h2>
                    </div>}
                {player2Nickname && !showLogin &&
                    <div className={`${styles.welcomeMessage}`}>
                        <h2>Welcome home, {player2Nickname}!</h2>
                    </div>}ls

                {!showLogin && isLoggedIn && (<div className={stylesLogin.container}>
                    {/*<button className={stylesLogin.button} onClick={handlePlayButtonClick}>Play</button>*/}
                    <div className={stylesLogin.container}>
                        <button className={stylesLogin.button} onClick={handlePlayButtonClick}>Play</button>
                    </div>
                </div>)}
                {showLogin && (<div className={stylesLogin.container}>
                    <div className={stylesLogin.formCard}>
                        <h1 className={`${stylesLogin.typingEffect} ${stylesLogin.typingEffectLine1}`}>Welcome
                            Back!</h1>
                        <p className={`${stylesLogin.typingEffect} ${stylesLogin.typingEffectLine2}`}>Log into your
                            account</p>
                        {loginError && <p className={stylesLogin.error}>{loginError}</p>}
                        <form onSubmit={handleLoginSubmit} className={stylesLogin.form}>
                            <div className={stylesLogin.inputContainer}>
                                <span className={stylesLogin.arrow}>→</span>
                                <input
                                    id="nickname"
                                    type="text"
                                    value={nickname}
                                    onChange={(e) => setNickname(e.target.value)}
                                    placeholder="Enter your nickname"
                                    className={stylesLogin.input}
                                    required
                                    onInvalid={(e) => e.target.setCustomValidity('Please, enter your nickname')}
                                    onInput={(e) => e.target.setCustomValidity('')}
                                />
                            </div>
                            <div className={stylesLogin.inputContainer}>
                                <span className={stylesLogin.arrow}>→</span>
                                <input
                                    id="password"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="Enter your password"
                                    className={stylesLogin.input}
                                    required
                                    onInvalid={(e) => e.target.setCustomValidity('Please, enter your password')}
                                    onInput={(e) => e.target.setCustomValidity('')}
                                />
                                <button
                                    type="button"
                                    onClick={handlePasswordVisibility}
                                    className={stylesLogin.showPasswordButton}
                                >
                                    {showPassword ? 'Hide' : 'Show'}
                                </button>
                            </div>
                            <button type="submit" className={stylesLogin.button}>Log In</button>
                            <div className={styles.buttonContainer}>
                                <button onClick={handleBackClick} className={styles.backButton}>Back</button>
                            </div>
                        </form>
                        {loginError === 'No such user found. Would you like to sign up?' && (
                            <button onClick={() => {
                                setShowLogin(false);
                                setShowSignup(true);
                            }} className={stylesLogin.smallButton}>Sign Up</button>)}
                    </div>
                </div>)}
                {showSignup && (<div className={stylesSignup.container}>
                    {(<div className={stylesSignup.formCard}>
                        <h1 className={`${stylesSignup.typingEffect} ${stylesSignup.typingEffectLine1}`}>Welcome
                            to Checkers!</h1>
                        <p className={` ${stylesSignup.typingEffect} ${stylesSignup.typingEffectLine2}`}>Let's
                            begin the adventure</p>
                        <form onSubmit={handleSignupSubmit} className={stylesSignup.form}>
                            <div className={stylesSignup.inputContainer}>
                                <span className={stylesSignup.arrow}>→</span>
                                <input
                                    id="nickname"
                                    type="text"
                                    value={nickname}
                                    onChange={handleNicknameChange}
                                    placeholder="Enter your nickname"
                                    className={stylesSignup.input}
                                    required
                                    onInvalid={(e) => e.target.setCustomValidity('Please, enter your nickname')}
                                    onInput={(e) => e.target.setCustomValidity('')}
                                />
                            </div>
                            {isNicknameAvailable === false && (
                                <p className={stylesSignup.error}>Nickname is already taken, please choose
                                    another one.</p>)}
                            {isNicknameAvailable && (<>
                                <div className={stylesSignup.inputContainer}>
                                    <span className={stylesSignup.arrow}>→</span>
                                    <input
                                        id="password"
                                        type={showPassword ? "text" : "password"}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        placeholder="Create a password"
                                        className={stylesSignup.input}
                                        required
                                        onInvalid={(e) => e.target.setCustomValidity('Please, enter your password')}
                                        onInput={(e) => e.target.setCustomValidity('')}
                                    />
                                    <button
                                        type="button"
                                        onClick={handlePasswordVisibility}
                                        className={stylesSignup.showPasswordButton}
                                    >
                                        {showPassword ? 'Hide' : 'Show'}
                                    </button>
                                </div>
                                <button type="submit" className={stylesSignup.button}>Sign Up</button>
                            </>)}
                            {isNicknameAvailable === null && (
                                <button type="submit" className={stylesSignup.button}>Sign up</button>)}
                            <div className={styles.buttonContainer}>
                                <button onClick={handleBackClick} className={styles.backButton}>Back</button>
                            </div>
                        </form>
                        {loginError === 'No such user found. Would you like to sign up?' && (
                            <button onClick={() => {
                                setShowSignup(false);
                                setShowLogin(true);
                            }} className={stylesLogin.smallButton}>
                                Already registered? Log in
                            </button>)}
                    </div>)}
                </div>)}
            </div>
            <div className="signup-stars">
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
                <div className="star"></div>
            </div>
        </Background>
    );


};

export default MainMenu;