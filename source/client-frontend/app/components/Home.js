import React, { Component } from 'react';
import { connect } from 'react-redux';

class Home extends Component {
  render() {
    return (
      <article>
        <div>
          <section className="text-section">
            <h1>Welcome to the IOSL Blockchain Identity project!</h1>
            <p>This application demonstrates what a blockchain-based approach to digital identity might look like.</p>
            <p>You may currently log in, register or go to the dashboard</p>
          </section>
        </div>
      </article>
    );
  }
}

function select(state) {
  return {
    data: state,
  };
}

export default connect(select)(Home);
