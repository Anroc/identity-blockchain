import React, { Component } from 'react';
// import request from '../auth/request';

class Government extends Component {
  constructor() {
    super();
    this.state = {
    };
  }

  componentDidMount() {
    console.log('government component mounted');
  }

  render() {
    return (
      <article>
        <section className="text-section">
          <h1>Government UI</h1>
          <p>
            Welcome!
          </p>

        </section>
      </article>
    );
  }
}

export default Government;
