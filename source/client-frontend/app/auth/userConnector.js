import 'whatwg-fetch';
import request from 'request';
const getOptions = {
  method: 'GET',
  headers: {
    Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  mode: 'cors',
  credentials: 'include',
};

const postOptions = {
  method: 'POST',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    password: 'timsDickerDick',
  }),
  mode: 'cors',
  credentials: 'include',

};

const userConnector = {

  /**
   * initializes options for server connections
   */
  init() {

  },

  /**
   *
   * @param password
   */
  login(password) {
    console.log(`loggin in with ${password}`);
    // TODO
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        password: 'timsDickerDick',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/login', options)
      .then((json) => {
        console.log(`login content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
      });
    console.log(`actual login ${actualRequest}`);
  },
  /**
   *
   * @param password
   */
  register(password) {
    console.log(`loggin in with ${password}`);
    return fetch('http://srv01.snet.tu-berlin.de:1112/account/register', postOptions);

    /*
      .then((json) => {
        console.log(`content' + ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
          ethID: json.ethereumID,
        });
      })
      .then(() => {
        this.getUserInformation();
      });
     */
  },
  logout() {
    console.log('log out user');
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({}),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/logout', options)
      .then((json) => {
        console.log(`logout content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
      });
    console.log(`actual logout ${actualRequest}`);
  },

  claims() {
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({}),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/logout', options)
      .then((json) => {
        console.log(`logout content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
      });
    console.log(`actual logout ${actualRequest}`);
  },
};

export default userConnector;
