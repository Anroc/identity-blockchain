import React, { Component } from 'react';
import request from '../auth/request';

class ThirdParty extends Component {
  constructor() {
    super();
    this.state = {
    };
  }

  componentDidMount() {
    console.log('TODO: give option to request permission from user');
    console.log('TODO: give section where retrieved data is displayed');
  }

  // todo change password
  // todo error labelling
  // todo claims erst spaeter
  // todo warning no recovery possible, keep your password safe
  // todo provider user anlegen etc von 8100
  // solved research standalone version for react

  displayClaims() {
    // TODO:
    // periodic pplling
  }

  clickSendRequestButton() {
    this.sendPermissionRequest();
  }

  sendPermissionRequest() {
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

    // TODO fix url
    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/permission/???', options)
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
          <h1>Third Party</h1>
          <p>
            Hello, third party!
          </p>
          <p>
            Permission request form e.g.
            GET isOver18 from timo
          </p>
          <p>
            If clicked, show result
          </p>
        </section>
      </article>
    );
  }
}

export default ThirdParty;
