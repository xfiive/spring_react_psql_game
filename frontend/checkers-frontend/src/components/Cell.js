import React from 'react';

const Cell = ({isBlack, children}) => {
    const backgroundColor = isBlack ? 'black' : 'white';
    const color = isBlack ? 'white' : 'black';
    return (
        <div style={{
            backgroundColor,
            color,
            width: '50px',
            height: '50px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
        }}>
            {children}
        </div>
    );
};

export default Cell;
