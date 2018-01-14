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
    console.log('hi provider, component mounted');
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
