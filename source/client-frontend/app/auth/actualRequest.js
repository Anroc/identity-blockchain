import serverConnector from './serverConnector';

serverConnector.init();

const actualRequest = {
  /**
   * Pretends to post to a remote server
   * @param  {string}  endpoint The endpoint of the server that should be contacted
   * @param  {?object} data     The data that should be transferred to the server
   */
  post(endpoint, data) {
    switch (endpoint) {
      case '/login':
        return serverConnector.login(data.password);
      case '/register':
        return serverConnector.register(data.password);
      case '/logout':
        return serverConnector.logout();
      default:
        return null;
    }
  },
};

export default actualRequest;
