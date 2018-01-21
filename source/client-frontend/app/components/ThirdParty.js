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
