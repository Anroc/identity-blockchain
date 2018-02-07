import React from 'react';
import PropTypes from 'prop-types';

class Welcome extends React.Component {
  render() {
    return (
      <div>
        <h1>Dashboard</h1>
      </div>
    );
  }
}

Welcome.propTypes = {
  ethID: PropTypes.string,
};

export default Welcome;
