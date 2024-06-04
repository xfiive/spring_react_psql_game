import React, {useContext, useEffect} from 'react';
import ValueContext from './ValueContext';
import darkSquare from '../css/dark-wood-cell.png';
import lightSquare from '../css/light-wood-cell.png';
import checkerBlack from '../css/checker-black.png';
import checkerWhite from '../css/checker-white.png';

const squareSize = 64;
const boardSize = 8;
const notationSize = 20;

const squareStyle = (backgroundImage) => ({
    width: `${squareSize}px`,
    height: `${squareSize}px`,
    backgroundImage: `url(${backgroundImage})`,
    backgroundSize: 'cover',
    zIndex: 2,
});

const notationColumnStyle = {
    position: 'absolute',
    display: 'flex',
    justifyContent: 'space-around',
    alignItems: 'center',
    color: 'white',
    fontWeight: 'bold',
    left: 0,
    bottom: 0,
    right: 0,
    width: '100%',
    height: `${notationSize}px`,
    paddingLeft: '10px',
    paddingRight: '40px',
    zIndex: 2,
};

const notationRowStyle = {
    position: 'absolute',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-around',
    alignItems: 'center',
    color: 'white',
    fontWeight: 'bold',
    left: 0,
    top: 0,
    bottom: 0,
    width: `${notationSize}px`,
    height: '100%',
    paddingTop: '55px',
    paddingBottom: '0px',
    zIndex: 2,
    paddingRight: '10px',
};

const containerStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: `${boardSize * squareSize + notationSize * 2}px`,
    height: `${boardSize * squareSize + notationSize * 2}px`,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '18px',
    flexDirection: 'column',
    zIndex: 2,
};

const boardStyle = {
    display: 'grid', gridTemplateColumns: `repeat(${boardSize}, 1fr)`, gridTemplateRows: `repeat(${boardSize}, 1fr)`,
    zIndex: 2,
};

const Board = ({handleCheckerClick}) => {
        const {initialCheckers: checkers} = useContext(ValueContext);

        useEffect(() => {
            console.log('Checkers state has changed:', checkers);
        }, [checkers]);

        const renderChecker = (id) => {
            const checker = checkers.find(c => c.id === id);
            if (checker) {
                const checkerImage = checker.color === 'black' ? checkerBlack : checkerWhite;
                return (
                    <img
                        src={checkerImage}
                        alt={`${checker.color} checker at ${id}`}
                        style={{width: '100%', height: '100%'}}
                        onClick={() => handleCheckerClick(id, checker.color)}
                    />
                );
            }
            return null;
        };

        const renderSquare = (i, isLight) => {
            const x = 'abcdefgh'[i % boardSize];
            const y = boardSize - Math.floor(i / boardSize);
            const id = `${x}${y}`;
            const backgroundImage = isLight ? lightSquare : darkSquare;

            const checkerComponent = renderChecker(id);

            return (
                <div
                    key={id}
                    style={squareStyle(backgroundImage)}
                    onClick={() => handleCheckerClick(id, checkerComponent ? checkerComponent.props.alt.split(' ')[0] : 'empty')}
                >
                    {checkerComponent}
                </div>
            );
        };
        const renderBoard = () => {
            const squares = [];
            for (let i = 0; i < boardSize * boardSize; i++) {
                const x = i % boardSize;
                const y = Math.floor(i / boardSize);
                const isLight = (x + y) % 2 === 0;
                squares.push(renderSquare(i, isLight));
            }
            return squares;
        };


        return (<div style={containerStyle}>
            <div style={notationRowStyle}>
                {['8', '7', '6', '5', '4', '3', '2', '1'].map((num) => (
                    <div key={num} style={{height: `${squareSize}px`, textAlign: 'center'}}>
                        {num}
                    </div>))}
            </div>
            <div style={{...boardStyle, border: '2px solid black'}}>
                {renderBoard()}
            </div>
            <div style={notationColumnStyle}>
                {['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'].map((letter) => (
                    <div key={letter} style={{width: `${squareSize}px`, textAlign: 'center'}}>
                        {letter}
                    </div>))}
            </div>
        </div>);
    }
;

export default Board;
