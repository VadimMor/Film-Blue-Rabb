import React, { Component } from 'react';
import Menu from '../../components/Menu/Menu';
import Recommendation from '../../components/Recommendation/Recommendation';

function Main() {
    return (
        <div className='container'>
            <Menu />

            <Recommendation />
        </div>
    )
}

export default Main