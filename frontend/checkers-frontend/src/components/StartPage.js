import React from 'react';
import {useNavigate} from 'react-router-dom';
import styles from './StartPage.module.css';


const StartPage = () => {
    const navigate = useNavigate();

    const handlePlay = () => {
        navigate("/menu/start");
    };

    const handleTopPlayers = () => {
        navigate("/hof");
    };

    const handleAbout = () => {
        navigate("/about");
    };

    const handleBackgroundClick = (event) => {
        const star = event.target.closest('.star');
        if (!star) {
            // console.log('in handler');
            const posX = event.clientX;
            const posY = event.clientY;
            const newStar = document.createElement('div');
            newStar.className = 'star';
            newStar.style.left = `${posX}px`;
            newStar.style.top = `${posY}px`;
            document.querySelector('.signup-stars').appendChild(newStar);

            setTimeout(() => {
                newStar.style.transform = 'scale(2)';
                setTimeout(() => {
                    newStar.style.transform = 'scale(1)';
                    setTimeout(() => {
                        newStar.remove();
                    }, 500);
                }, 500);
            }, 10);
        }
    };


    return (
        <div className={styles.background} onClick={handleBackgroundClick}>
            <div className={styles.menu}>
                <button onClick={handlePlay} className={styles.menuButton}>Play</button>
                <button onClick={handleTopPlayers} className={styles.menuButton}>Top Players</button>
                <button onClick={handleAbout} className={styles.menuButton}>About</button>
            </div>
            <div className="signup-stars">
                {[...Array(30)].map((_, index) => (
                    <div key={index} className="star"></div>
                ))}
            </div>
        </div>
    );
};

export default StartPage;
