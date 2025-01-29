import { NavLink } from 'react-router';
import { useState } from 'react';

import classes from './Header.module.css'
import '../../assets/fonts/iconmoon/style.css'

function Header() {
    const [token, setToken] = useState(false);
    const [menu, setMenu] = useState(false);

    function activeMenu() {
        setMenu(!menu)
    }

    return (
        <header className={menu ? classes.active : null}>
            <div className={classes.container}>
                <NavLink 
                className={classes.logo}
                to="/"
                alt="Главная">
                    film rabb
                </NavLink>

                <div className={classes.desc}>
                    <div className={classes.search}>
                        <input type='search'  placeholder='Поиск'/>
                        <span className={`${classes.icon} icon-search`} />
                    </div>

                    {
                        token ? (
                            <>
                                <div className={classes.menu}>
                                    <NavLink 
                                        to="/likes"
                                        className={classes.link+" icon-heart"}
                                        alt="Избранное"
                                        title="Избранное"
                                    />
                                </div>

                                <NavLink 
                                    to="/profile"
                                    className={classes.link+" icon-user"}
                                    alt="Профиль"
                                    title="Профиль"
                                />
                            </>
                        ) : (
                            <NavLink 
                                to="/auth"
                                className={classes.link+" icon-enter"}
                                alt="Вход"
                                title="Вход"
                            />
                        )
                    }
                </div>

                <button className={classes.burger} onClick={activeMenu}>
                    <span />
                    <span />
                    <span />
                </button>
            </div>

            <div className={classes.mobile}>
                <div className={classes.search}>
                    <input type='search'  placeholder='Поиск'/>
                    <span className={`${classes.icon} icon-search`} />
                </div>

                {
                    token ? (
                        <>
                            <div className={classes.menu}>
                                <NavLink 
                                    to="/likes"
                                    className={classes.link}
                                    alt="Избранное"
                                    title="Избранное"
                                >
                                    <span className="icon-heart" />
                                    Избранное
                                </NavLink>
                            </div>

                            <NavLink 
                                to="/profile"
                                className={classes.link}
                                alt="Профиль"
                                title="Профиль"
                            >
                                <span className="icon-user" />
                                Профиль
                            </NavLink>
                        </>
                    ) : (
                        <NavLink 
                            to="/auth"
                            className={classes.link}
                            alt="Вход"
                            title="Вход"
                        >
                            <span className="icon-enter" />
                            Вход
                        </NavLink>
                    )
                }
            </div>
        </header>
    )
}

export default Header;