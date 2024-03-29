import 'babel-polyfill';

import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, browserHistory } from 'react-router';
import { createStore, applyMiddleware } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { Provider } from 'react-redux';
import { createLogger } from 'redux-logger';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import createMuiTheme from 'material-ui/styles/createMuiTheme';
import { grey, amber, red } from 'material-ui/colors';

import reducer from './reducers';
import rootSaga from './sagas';
import { clearError } from './actions';

import './styles/main.css';

import App from './components/App';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import User from './components/User';
import NotFound from './components/NotFound';
import Government from './components/Government';
import Bank from './components/Bank';

const logger = createLogger({
  // Ignore `CHANGE_FORM` actions in the logger, since they fire after every keystroke
  predicate: (getState, action) => action.type !== 'CHANGE_FORM',
});

const sagaMiddleware = createSagaMiddleware();

// Creates the Redux store using our reducer and the logger and saga middlewares
const store = createStore(reducer, applyMiddleware(logger, sagaMiddleware));
// We run the root saga automatically
sagaMiddleware.run(rootSaga);

/**
* Checks authentication status on route change
* @param  {object}   nextState The state we want to change into when we change routes
* @param  {function} replace Function provided by React Router to replace the location
*/
function checkAuth(nextState, replace) {
  const { loggedIn } = store.getState();

  store.dispatch(clearError());

  // Check if the path isn't user. That way we can apply specific logic to
  // display/render the path we want to
  if (nextState.location.pathname !== '/user') {
    if (loggedIn) {
      if (nextState.location.state && nextState.location.pathname) {
        replace(nextState.location.pathname);
      } else {
        replace('/');
      }
    }
  } else {
    // If the user is already logged in, forward them to the homepage
    if (!loggedIn) {
      if (nextState.location.state && nextState.location.pathname) {
        replace(nextState.location.pathname);
      } else {
        replace('/');
      }
    } else {
      console.log('is logged in');
    }
  }
}


const muiTheme = createMuiTheme({
  palette: {
    primary: grey,
    accent: amber,
    error: red,
    type: 'light',
  },
});

// Mostly boilerplate, except for the routes. These are the pages you can go to,
// which are all wrapped in the App component, which contains the navigation etc
class LoginFlow extends Component {

  constructor() {
    super();
    this.state = {
      ethID: '',
    };
  }

  render() {
    const setEthId = (ethID) => {
      console.log('hey');
      console.log(`updating eth id in app to: ${ethID}`, ethID);
      this.setState({
        ethID,
      });
    };

    return (
      <MuiThemeProvider theme={muiTheme}>
        <Provider store={store}>
          <Router history={browserHistory}>
            <Route component={App}>
              <Route path="/" component={Home} />
              <Route onEnter={checkAuth}>
                <Route path="/login" component={Login} />
                <Route
                  path="/register"
                  component={() => (
                    <Register
                      ethId={this.state.ethID}
                      setEthId={setEthId}
                    />)}
                />
                <Route
                  path="/user"
                  component={() => (
                    <User
                      ethID={this.state.ethID}
                    />)}
                />
              </Route>
              <Route path="/government" component={Government} />
              <Route path="/bank" component={Bank} />
              <Route path="*" component={NotFound} />
            </Route>
          </Router>
        </Provider>
      </MuiThemeProvider>
    );
  }
}

ReactDOM.render(<LoginFlow />, document.getElementById('app'));
