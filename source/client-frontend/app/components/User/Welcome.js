import React from 'react';
import PropTypes from 'prop-types';

class Welcome extends React.Component {
  render() {
    return (
      <div>
        <h1>Dashboard</h1>
        <p>
          Welcome, you are logged in!
        </p>
        <p>
          General:
          {this.props.ethID}
        </p>
      </div>
    );
  }
}

Welcome.propTypes = {
  ethID: PropTypes.string,
};

export default Welcome;
