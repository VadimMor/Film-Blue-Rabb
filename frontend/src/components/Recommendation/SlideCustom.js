import { NavLink } from 'react-router';
import classes from './SlideCustom.module.css'

function SlideCustom({content}) {
    return (
        <NavLink 
            className={classes.content}
            to={`/${content.type}?name=${content.shortName}`}
            alt={content.fullName}
            title={content.fullName}
        >
            <img src='https://loremflickr.com/928/480' alt={content.fullName} loading="lazy" className={classes.img}/>
            <div className={classes.text}>
                {content.fullName}
            </div>
        </NavLink>
    )
}

export default SlideCustom;