/**
* Fake XMLHttpRequest wrapper
*/

import server from './fakeServer';

server.init();

const fakeRequest = {
  /**
  * Pretends to post to a remote server
  * @param  {string}  endpoint The endpoint of the server that should be contacted
  * @param  {?object} data     The data that should be transferred to the server
  * @param  {string}  data.username
  * @param  {string}  data.password
   * @param {string} data.accountType
  */
  post(endpoint, data) {
    switch (endpoint) {
      case '/login':
        return server.login(data.username, data.password, data.accountType);
      case '/register':
        return server.register(data.username, data.password, data.accountType);
      case '/logout':
        return server.logout();
      default:
        return null;
    }
  },
};

export default fakeRequest;
