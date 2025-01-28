import { NavLink } from 'react-router';
import classes from './Menu.module.css';

function Menu() {
    return (
        <div className={classes.menu}>
            <NavLink
                to="/"
                alt='Главная'
                className={({ isActive }) => isActive ? `${classes.link} ${classes.active}` : `${classes.link}`}
            >
                Главная
            </NavLink>
            <NavLink
                to="/movies"
                alt='Фильмы'
                className={({ isActive }) => isActive ? `${classes.link} ${classes.active}` : `${classes.link}`}
            >
                Фильмы
            </NavLink>
            <NavLink
                to="/series"
                alt='Сериалы'
                className={({ isActive }) => isActive ? `${classes.link} ${classes.active}` : `${classes.link}`}
            >
                Сериалы
            </NavLink>
            <NavLink
                to="/cartoons"
                alt='Мультфильмы'
                className={({ isActive }) => isActive ? `${classes.link} ${classes.active}` : `${classes.link}`}
            >
                Мультфильмы
            </NavLink>
            <NavLink
                to="/likes"
                alt='Избранное'
                className={({ isActive }) => isActive ? `${classes.link} ${classes.active}` : `${classes.link}`}
            >
                Избранное
            </NavLink>
        </div>
    )
}

export default Menu;