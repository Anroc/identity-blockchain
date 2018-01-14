import React, { Component } from 'react';
import { connect } from 'react-redux';
import 'whatwg-fetch';
import Nav from './common/Nav';
import request from '../auth/request';

/*
const testRequest = request('http://jsonplaceholder.typicode.com/posts')
  .then((json) => {
    console.log(JSON.stringify(json[0]))
  })
console.log(testRequest)

const options = {
  method: 'GET',
  headers: {
    'Authorization': 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  },
  mode: 'cors',
  credentials: 'include'
}

const actualRequest = request('http://srv01.snet.tu-berlin.de:8100/v2/api-docs', options)
  .then((json) => {
    console.log('content' + JSON.stringify(json))
  })

console.log('actual' + actualRequest)
*/

class App extends Component {
  render() {
    return (
      <div className="wrapper">
        <Nav
          loggedIn={this.props.data.loggedIn}
          currentlySending={this.props.data.currentlySending}
          history={this.props.history}
          dispatch={this.props.dispatch}
          location={this.props.location}
        />
        {this.props.children}
      </div>
    );
  }
}

App.propTypes = {
  data: React.PropTypes.object,
  history: React.PropTypes.object,
  location: React.PropTypes.object,
  children: React.PropTypes.object,
  dispatch: React.PropTypes.func,
};

function select(state) {
  return {
    data: state,
  };
}

export default connect(select)(App);
