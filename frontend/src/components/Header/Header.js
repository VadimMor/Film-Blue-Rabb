import { NavLink } from 'react-router';

import classes from './Header.module.css'
import '../../assets/fonts/iconmoon/style.css'
import search from '../../assets/icon/search.svg'
import { useState } from 'react';

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
                to="/">
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
                                    />
                                </div>

                                <NavLink 
                                    to="/profile"
                                    className={classes.link+" icon-user"}
                                />
                            </>
                        ) : (
                            <NavLink 
                                to="/auth"
                                className={classes.link+" icon-enter"}
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
                    !token ? (
                        <>
                            <div className={classes.menu}>
                                <NavLink 
                                    to="/likes"
                                    className={classes.link}
                                >
                                    <span className="icon-heart" />
                                    Избранное
                                </NavLink>
                            </div>

                            <NavLink 
                                to="/profile"
                                className={classes.link}
                            >
                                <span className="icon-user" />
                                Профиль
                            </NavLink>
                        </>
                    ) : (
                        <NavLink 
                            to="/auth"
                            className={classes.link}
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