import React from 'react';

import { getApi } from '../../shared/API/api';
import { setCookie } from '../../shared/cookie/setCookie';
import { getCookie } from '../../shared/cookie/getCookie';

const api = getApi();

const Home = () => {
  console.log(getCookie('authToken'));
  return <div className='wrapper'>
    Amet officia magna mollit incididunt esse sunt proident aute. Lorem velit nulla enim labore consequat. Ipsum exercitation laborum qui amet eiusmod. Amet commodo proident anim fugiat eiusmod sunt sunt commodo cillum consequat est sunt.
  </div>;
};

export default Home;