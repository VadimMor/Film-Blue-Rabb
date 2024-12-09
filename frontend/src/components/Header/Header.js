import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import NavLinks  from './NavLink/NavLinks';

import classes from './Header.module.css'

const Header = () => {
    const [active, setActive] = useState(false);

    function toggleMenu () {
        setActive(!active);
    }

    return (
        <header className={
            active ? classes.active : null
        }>
            <div className={classes.wrapper}>
                <Link
                    to='/'
                    className={classes.link_logo}
                >
                    <div className={classes.icon + ' icon-rabbit'}/>
                    <div className={classes.link_logo_text}>Blue Rabbit</div>
                </Link>

                <div className={classes.nav_root}>
                    <input
                        className={classes.search}
                        type='search'
                        placeholder='Поиск'
                    />

                    <nav className={classes.nav}>
                        <NavLinks 
                            path={'/gift'}
                            icon={{name: 'gift', title: 'Подакри'}}
                            number={1}
                        />
                        <NavLinks 
                            path={'/notification'}
                            icon={{name: 'notification', title: 'Уведомления'}}
                            number={10}
                        />
                        <NavLinks 
                            path={'/porfile'}
                            icon={{name: 'user', title: 'Профиль'}}
                        />
                    </nav>
                </div>

                <div className={classes.mobile_nav}>
                    <input
                        className={classes.search}
                        type='search'
                        placeholder='Поиск'
                    />

                    <nav className={classes.nav}>
                        <NavLinks 
                            path={'/gift'}
                            icon={{name: 'gift', title: 'Подакри'}}
                            number={1}
                            text={'Подарки'}
                        />
                        <NavLinks 
                            path={'/notification'}
                            icon={{name: 'notification', title: 'Уведомления'}}
                            number={10}
                            text={'Уведомления'}
                        />
                        <NavLinks 
                            path={'/porfile'}
                            icon={{name: 'user', title: 'Профиль'}}
                            text={'Профиль'}
                        />
                    </nav>
                </div>

                <button className={classes.btn_menu} onClick={toggleMenu}/>
            </div>
        </header>
    )
}

export default Header;