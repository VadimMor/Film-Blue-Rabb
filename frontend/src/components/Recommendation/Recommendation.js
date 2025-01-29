import { Swiper, SwiperSlide  } from 'swiper/react';
import { Navigation, Pagination, Autoplay } from 'swiper/modules';

import 'swiper/css';
import 'swiper/swiper-bundle.css'
import classes from './Recommendation.module.css'

import SlideCustom from './SlideCustom';

function Recommendation() {
    const sliders = [
        {
            img: "name_image",
            fullName: "Name video",
            shortName: "name_video",
            type: "movie"
        },
        {
            img: "name_image",
            fullName: "Name video",
            shortName: "name_video",
            type: "movie"
        },
        {
            img: "name_image",
            fullName: "Name video",
            shortName: "name_video",
            type: "movie"
        },
        {
            img: "name_image",
            fullName: "Name video",
            shortName: "name_video",
            type: "movie"
        }
    ]
    
    return (
        <div className='PD'>
            <Swiper
                modules={[Pagination, Autoplay]}
                spaceBetween={0}
                slidesPerView={1}
                loop={true}
                pagination={{
                    bulletActiveClass: classes.swiper_pagination_active,
                    clickable: true
                }}
                scrollbar={{ draggable: true }}
                autoplay={{
                    delay: 10000,
                    disableOnInteraction: false,
                }}
            >
                {
                    sliders.map((content, index) => (
                        <SwiperSlide virtualIndex={index}>
                            <SlideCustom
                                content={content}
                            />                            
                        </SwiperSlide>
                    ))
                }
            </Swiper>
        </div>
    )
}

export default Recommendation;