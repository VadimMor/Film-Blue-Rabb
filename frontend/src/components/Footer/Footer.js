import React from "react";

import NavLinks from './NavLink/NavLinks';

import classes from './Footer.module.css';
import Social from "./Social/Social";

const Footer = () => {
    return (
        <footer>
            <div className={classes.footer_wrapper}>
                <nav className={classes.nav}>
                    <NavLinks 
                        path='/'
                        title='Главная'
                    />
                    
                    <NavLinks 
                        path='/help'
                        title='Помощь'
                    />
                    <NavLinks 
                        path='/about'
                        title='О нас'
                    />
                    <NavLinks 
                        path='/privacy'
                        title='Конфиденциальность'
                    />
                </nav>

                <nav className={classes.social}>
                    <Social
                        path='https://www.instagram.com'
                        icon='facebook'
                        title='Facebook'
                    />
                    <Social
                        path='https://www.facebook.com'
                        icon='instagram'
                        title='Instagram'
                    />
                </nav>
            </div>
        </footer>
    )
}

export default Footer