import React, { Component } from 'react';
import { connect } from 'react-redux';

class Home extends Component {
  render() {
    return (
      <article>
        <div>
          <section className="text-section">
            <h1>Welcome to the IOSL project: Identity in the Blockchain!</h1>
            <p>This application demonstrates what a blockchain-based approach to digital identity might look like.</p>
            <p>You may currently log in or register as a user, provider or third party.</p>
            <p>Then you can respectively interact with the system.</p>
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
