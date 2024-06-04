import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import styles from './SignupForm.module.css';

const SignupForm = ({onSuccess, onUserNotFound}) => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [isNicknameAvailable, setIsNicknameAvailable] = useState(null);
    const [isRegistered, setIsRegistered] = useState(false);
    const [loginError, setLoginError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handlePasswordVisibility = () => {
        setShowPassword(!showPassword);
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

    const signUp = async (nickname, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/player/checkers/sign-up', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({nickname, password}),
            });
            if (response.ok) {
                const playerData = await response.json();
                onSuccess(playerData.nickname);
                setIsRegistered(true);
                return playerData;
            } else {
                console.error('Registration failed:', response.status);
                onUserNotFound();
                setIsRegistered(false);
            }
        } catch (error) {
            console.error('There was an error during registration:', error);
            onUserNotFound();
            setIsRegistered(false);
        }
    };

    const handleNicknameChange = async (e) => {
        const newNickname = e.target.value;
        setNickname(newNickname);
        if (newNickname) {
            const exists = await checkNickname(newNickname);
            setIsNicknameAvailable(!exists);
        } else {
            setIsNicknameAvailable(null);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!nickname) {
            setIsNicknameAvailable(null);
            return;
        }
        if (isNicknameAvailable) {
            await signUp(nickname, password);
        }
    };

    return (
        <div className={styles.container}>
            {!isRegistered ? (
                <div className={styles.formCard}>
                    <h1 className={`${styles.typingEffect} ${styles.typingEffectLine1}`}>Welcome to
                        Checkers!</h1>
                    <p className={` ${styles.typingEffect} ${styles.typingEffectLine2}`}>Let's begin
                        the adventure</p>
                    <form onSubmit={handleSubmit} className={styles.form}>
                        <div className={styles.inputContainer}>
                            <span className={styles.arrow}>→</span>
                            <input
                                id="nickname"
                                type="text"
                                value={nickname}
                                onChange={handleNicknameChange}
                                placeholder="Enter your nickname"
                                className={styles.input}
                                required
                                onInvalid={(e) => e.target.setCustomValidity('Please, enter your nickname')}
                                onInput={(e) => e.target.setCustomValidity('')}
                            />
                        </div>
                        {isNicknameAvailable === false && (
                            <p className={styles.error}>Nickname is already taken, please choose another one.</p>
                        )}
                        {isNicknameAvailable && (
                            <>
                                <div className={styles.inputContainer}>
                                    <span className={styles.arrow}>→</span>
                                    <input
                                        id="password"
                                        type={showPassword ? "text" : "password"}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        placeholder="Create a password"
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
                                <button type="submit" className={styles.button}>Sign Up</button>
                            </>
                        )}
                        {isNicknameAvailable === null && (
                            <button type="submit" className={styles.button}>Sign up</button>
                        )}
                        <button type="button" onClick={() => navigate('/login')} className={styles.smallButton}>
                            Already registered? Log in
                        </button>
                    </form>
                    {loginError === 'No such user found. Would you like to sign up?' && (
                        <button onClick={() => navigate('/menu')} className={styles.smallButton}>
                            Already registered? Log in
                        </button>
                    )}
                </div>
            ) : (
                <div className={styles.welcomeCard}>
                    <button onClick={() => navigate('/menu')} className={styles.header}>Welcome to
                        Checkers, {nickname}!
                    </button>
                </div>
            )}
        </div>
    );

};

export default SignupForm;
