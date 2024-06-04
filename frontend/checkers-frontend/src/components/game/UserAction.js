import React from 'react';
import styles from './UserActions.module.css';

const UserActions = ({ onFindGame, onViewStats }) => {
    return (
        <div className={styles.container}>
            <button onClick={onFindGame} className={styles.button}>
                Find new game
            </button>
            <button onClick={onViewStats} className={styles.button}>
                See my stats
            </button>
        </div>
    );
};

export default UserActions;
