// Импорт стилей и реакт зависимостей как обычно
import React, {useEffect, useRef, useState} from 'react';
import styles from '../css/AudioPlayer.module.css'; // Обеспечьте правильный путь к вашим стилям

const AudioPlayer = () => {
    const audioRef = useRef(null);
    const [trackIndex, setTrackIndex] = useState(0);
    const [volume, setVolume] = useState(0.75);
    const [currentTime, setCurrentTime] = useState(0);

    const tracks = [
        "../../music/after-forever.mp3",
        "../../music/all-right-now.mp3",
        "../../music/black-night.mp3",
        "../../music/burn.mp3",
        "../../music/fire-and-water.mp3",
        "../../music/highway-star.mp3",
        "../../music/i-bet-my-life.mp3",
        "../../music/im-so-sorry.mp3",
        "../../music/iron-man.mp3",
        "../../music/money-for-nothing.mp3",
        "../../music/motherless-children.mp3",
        "../../music/natural.mp3",
        "../../music/paranoid.mp3",
        "../../music/polaroid.mp3",
        "../../music/ride-across.mp3",
        "../../music/route-66.mp3",
        "../../music/roxette.mp3",
        "../../music/rusted-from-the-rain.mp3",
        "../../music/smoke-on-the-water.mp3",
        "../../music/sunshine-of-your-love.mp3",
        "../../music/take-me-home.mp3",
        "../../music/thunder.mp3",
        "../../music/walk-in-my-shadow.mp3"
    ];


    useEffect(() => {
        playTrack();
        audioRef.current.volume = volume;
        const interval = setInterval(() => {
            if (audioRef.current) {
                setCurrentTime(audioRef.current.currentTime);
            }
        }, 1000);
        return () => clearInterval(interval);
    }, [trackIndex, volume]);

    const playTrack = () => {
        if (audioRef.current) {
            audioRef.current.src = tracks[trackIndex];
            audioRef.current.play().catch(error => console.log("Failed to play track: ", error));
        }
    };

    const handlePlayPause = () => {
        if (audioRef.current && audioRef.current.paused) {
            audioRef.current.play();
        } else {
            audioRef.current.pause();
        }
    };

    const handleNextTrack = () => {
        setTrackIndex((current) => (current + 1) % tracks.length);
    };

    const handlePrevTrack = () => {
        setTrackIndex((current) => (current === 0 ? tracks.length - 1 : current - 1));
    };

    const handleVolumeChange = (event) => {
        setVolume(event.target.value);
    };

    const handleProgressChange = (event) => {
        const time = (event.target.value / 100) * audioRef.current.duration;
        audioRef.current.currentTime = time;
        setCurrentTime(time);
    };

    return (
        <div className={styles.audioPlayer}>
            <div className={styles.controls}>
                <button className={styles.button} onClick={handlePrevTrack}>⏮</button>
                <button className={styles.button} onClick={handlePlayPause}>⏯</button>
                <button className={styles.button} onClick={handleNextTrack}>⏭</button>
                <input
                    type="range"
                    min="0"
                    max="1000"
                    value={(currentTime / audioRef.current?.duration) * 100 || 0}
                    onChange={handleProgressChange}
                    className={styles.progress}
                />
                <input
                    type="range"
                    min="0"
                    max="1"
                    step="0.01"
                    value={volume}
                    onChange={handleVolumeChange}
                    className={styles.volume}
                />
            </div>
            <audio ref={audioRef} onEnded={handleNextTrack} loop>
                <source src={tracks[trackIndex]} type="audio/mpeg"/>
            </audio>
        </div>
    );
};

export default AudioPlayer;
