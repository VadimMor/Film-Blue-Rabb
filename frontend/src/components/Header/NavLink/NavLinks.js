import { NavLink } from 'react-router-dom';

import classes from './NavLink.module.css'
import '@assets/fonts/iconmoon/style.css'

const NavLinks = ({path, icon, number, text}) => {
    return (
        <NavLink
            to={path}
            className={({ isActive }) =>
                isActive ? classes.nav_link+' '+classes.active : classes.nav_link
            }
            title={icon.title}
        >
            <div className={classes.icon + ' icon-'+icon.name}/>
            <div className={classes.mobile_text}>{text}</div>
            {number !== '' && number > 0 ? (
                    number < 10 ? (
                        <label className={classes.number}>{number}</label>
                    ) : (
                        <label className={classes.number}>9+</label>
                    )
                ) : (
                    null
                )
            }
        </NavLink>
    )
}

export default NavLinks;