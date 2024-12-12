import React from 'react';

import { getApi } from '../../shared/API/api';
import { setCookie } from '../../shared/cookie/setCookie';

const api = getApi();

const Home = () => {

  async function sdfsdfs() {
    const token = await api.getAuth(
      {
        "login": "rich505@bk.ru",
        "password": "Klmsdpre12!"
      }
    )

    // setCookie('token', token.token);

  }

  return <div className='wrapper'>
    Amet officia magna mollit incididunt esse sunt proident aute. Lorem velit nulla enim labore consequat. Ipsum exercitation laborum qui amet eiusmod. Amet commodo proident anim fugiat eiusmod sunt sunt commodo cillum consequat est sunt.
  </div>;
};

export default Home;