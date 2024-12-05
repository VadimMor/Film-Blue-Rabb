import React from 'react';
import { useState } from 'react';

import NavLinks from './NavLink/NavLinks';

import classes from './Header.module.css'

const Header = () => {
    const [active, setActive] = useState(false);

    function handleClick() {
        setActive(!active);
        console.log(active)
    }
    
    return (
        <div className={active ? classes.active : ''}>
            <header>
                <nav className={classes.nav}>
                    <NavLinks 
                        path={'/'}
                        icon={{name: 'rabbit', title: 'logo'}}
                        text={'Главная'}
                    />
                    <NavLinks 
                        path={'/settings'}
                        icon={{name: 'user', title: 'Профиль'}}
                        text={'Профиль'}
                    />
                </nav>

                <button
                    className={classes.burger}
                    onClick={handleClick}
                >
                    <span />    
                </button>
            </header>

            <div
                className={classes.bg}
                onClick={handleClick}
            />
        </div>
    )
}

export default Header;