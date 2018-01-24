import userConnector from './userConnector';

userConnector.init();

const actualRequest = {
  /**
   * Pretends to post to a remote server
   * @param  {string}  endpoint The endpoint of the server that should be contacted
   * @param  {?object} data     The data that should be transferred to the server
   * @param  {string}  data.password
   */
  post(endpoint, data) {
    switch (endpoint) {
      case '/login':
        return userConnector.login(data.password);
      case '/register':
        return userConnector.register(data.password);
      case '/logout':
        return userConnector.logout();
      default:
        return null;
    }
  },
};

export default actualRequest;
