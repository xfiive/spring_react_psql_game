import React from 'react';
import {useNavigate} from 'react-router-dom';
import styles from './About.module.css';




const About = () => {
    const navigate = useNavigate();

    const navigateToStartPage = () => {
        navigate('/menu');
    };

    const navigateToMainMenu = () => {
        navigate('/menu/start');
    };

    return (
        <div className={styles.aboutContainer}>
            <div className={styles.aboutBox}>
                <h1>About This Game</h1>
                <p>Welcome to our Checkers game! This is where strategy meets fun.</p>
                <p>Explore the depths of strategy in this classic game of Checkers. Whether you're a seasoned veteran or
                    a first-time player, there's always something new to learn and discover.</p>
                <p>Created with passion and dedication to bring you the best gaming experience.</p>
                <p>I have put dozens of sleepless nights into this project, and would love to hear any feedback you can
                    give me. Email me at mikhail.shytsko@student.tuke.sk with any of your suggestions for improving my
                    app. </p>
                <p>Copyright (C) 2024 Mikhail Shytsko
                    This file is part of the Checkers Game.

                    Checkers game is free software: you can redistribute it and/or modify
                    it under the terms of the GNU General Public License as published by
                    the Free Software Foundation, either version 3 of the License, or
                    (at your option) any later version.

                    Checkers Game is distributed in the hope that it will be useful,
                    but WITHOUT ANY WARRANTY; without even the implied warranty of
                    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
                    GNU General Public License for more details.

                    You should have received a copy of the GNU General Public License
                    along with Checkers Game. If not, see http:
                    //www.gnu.org/licenses/. </p>
                <div style={{
                    display: 'flex',
                    justifyContent: 'center',
                    gap: '20px',
                    marginTop: '20px'
                }}>
                    <button style={{
                        minWidth: '120px',
                        padding: '10px 20px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4C5C68',
                        color: 'white',
                        fontSize: '16px',
                        cursor: 'pointer'
                    }} onClick={navigateToStartPage}>Back to Start
                    </button>
                    <button style={{
                        minWidth: '120px',
                        padding: '10px 20px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4C5C68',
                        color: 'white',
                        fontSize: '16px',
                        cursor: 'pointer'
                    }} onClick={navigateToMainMenu}>Back to Menu
                    </button>
                </div>
            </div>
        </div>
    );
};

export default About;
