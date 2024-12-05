import { Link, NavLink } from 'react-router-dom';

import classes from './NavLink.module.css'
import '@assets/fonts/iconmoon/style.css'

const NavLinks = ({path, icon, text}) => {
    return (
        <NavLink
            to={path}
            className={({ isActive }) =>
                isActive ? classes.nav_link+' '+classes.active : classes.nav_link
            }
            title={text}
        >
            <div className={classes.icon + ' icon-'+icon.name}/>
            <div>{text}</div>
        </NavLink>
    )
}

export default NavLinks;