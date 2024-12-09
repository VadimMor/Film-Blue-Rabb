import { NavLink } from 'react-router-dom';

import classes from './NavLink.module.css'
import '@assets/fonts/iconmoon/style.css'

const NavLinks = ({path, icon, number}) => {
    if (number !== '' && number > 0) {
        if (number < 10) {
            return (
                <NavLink
                    to={path}
                    className={({ isActive }) =>
                        isActive ? classes.nav_link+' '+classes.active : classes.nav_link
                    }
                    title={icon.title}
                >
                    <div className={classes.icon + ' icon-'+icon.name}/>
                    <label className={classes.number}>{number}</label>
                </NavLink>
            )
        } else {
            return (
                <NavLink
                    to={path}
                    className={({ isActive }) =>
                        isActive ? classes.nav_link+' '+classes.active : classes.nav_link
                    }
                    title={icon.title}
                >
                    <div className={classes.icon + ' icon-'+icon.name}/>
                    <label className={classes.number}>9+</label>
                </NavLink>
            )
        }
    } else {
        return (
            <NavLink
                to={path}
                className={({ isActive }) =>
                    isActive ? classes.nav_link+' '+classes.active : classes.nav_link
                }
                title={icon.title}
            >
                <div className={classes.icon + ' icon-'+icon.name}/>
            </NavLink>
        )
    }
}

export default NavLinks;