import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import styles from './LoginForm.module.css';

const LoginForm = ({onSuccess, onUserNotFound}) => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [loginError, setLoginError] = useState('');
    const navigate = useNavigate();

    const handlePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const logIn = async (nickname, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/player/checkers/log-in', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({nickname, password}),
            });

            if (response.ok) {
                const playerData = await response.json();
                onSuccess(playerData.nickname);
                setIsLoggedIn(true);
                setLoginError('');
                return true;
            } else {
                setLoginError('Invalid password. Please try again.');
                onUserNotFound();
                return false;
            }
        } catch (error) {
            console.error('There was an error during login:', error);
        }
    };

    if (isLoggedIn) {
        return (
            <div className={styles.container}>
                <div className={styles.welcomeBackCard}>
                    <button onClick={() => navigate('/menu')} className={styles.header}>Welcome back home, {nickname}!
                    </button>
                    {/*<button onClick={() => navigate('/signup')} className={styles.smallButton}>Sign*/}
                    {/*    Up</button>*/}
                </div>
            </div>
        );
    }


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

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userExists = await checkForExistingPlayer(nickname);
        if (userExists) {
            const loggedIn = await logIn(nickname, password);
            if (!loggedIn) {
                setLoginError('Incorrect password. Please try again.');
            }
        } else {
            setLoginError('No such user found. Would you like to sign up?');
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.formCard}>
                <h1 className={`${styles.typingEffect} ${styles.typingEffectLine1}`}>Welcome Back!</h1>
                <p className={`${styles.typingEffect} ${styles.typingEffectLine2}`}>Log into your account</p>
                {loginError && <p className={styles.error}>{loginError}</p>}
                <form onSubmit={handleSubmit} className={styles.form}>
                    <div className={styles.inputContainer}>
                        <span className={styles.arrow}>→</span>
                        <input
                            id="nickname"
                            type="text"
                            value={nickname}
                            onChange={(e) => setNickname(e.target.value)}
                            placeholder="Enter your nickname"
                            className={styles.input}
                            required
                            onInvalid={(e) => e.target.setCustomValidity('Please, enter your nickname')}
                            onInput={(e) => e.target.setCustomValidity('')}
                        />
                    </div>
                    <div className={styles.inputContainer}>
                        <span className={styles.arrow}>→</span>
                        <input
                            id="password"
                            type={showPassword ? "text" : "password"}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            className={styles.input}
                            required
                            onInvalid={(e) => e.target.setCustomValidity('Please, enter your password')}
                            onInput={(e) => e.target.setCustomValidity('')}
                        />
                        <button
                            type="button"
                            onClick={handlePasswordVisibility}
                            className={styles.showPasswordButton}
                        >
                            {showPassword ? 'Hide' : 'Show'}
                        </button>
                    </div>
                    <button type="submit" className={styles.button}>Log In</button>
                </form>
                {loginError === 'No such user found. Would you like to sign up?' && (
                    <button onClick={() => navigate('/signup')} className={styles.smallButton}>Sign
                        Up</button>
                )}
            </div>
        </div>
    );
};

export default LoginForm;
