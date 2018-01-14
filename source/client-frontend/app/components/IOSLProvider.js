import React, { Component } from 'react';
import request from '../auth/request';

class Provider extends Component {
  constructor() {
    super();
    this.state = {
      swaggerData: '',
    };
  }

  componentDidMount() {
    console.log('sending request');
    this.sendRequest();
    console.log('request sent');
  }

  // todo change password
  // todo error labelling
  // claims erst spaeter
  // todo warning no recovery possible, keep your password safe
  // todo provider user anlegen etc von 8100

  sendRequest() {
    const optionsForServer = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };
    console.log(optionsForServer);

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

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((json) => {
        console.log(`content' + ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
        console.log(`content in state: ${this.state.swaggerData}`);
      });
    console.log(`actual ${actualRequest}`);
  }

  render() {
    return (
      <article>
        <section className="text-section">
          <h1>Provider UI</h1>
          <p>
            Welcome!
          </p>
        </section>
      </article>
    );
  }
}

export default Provider;
