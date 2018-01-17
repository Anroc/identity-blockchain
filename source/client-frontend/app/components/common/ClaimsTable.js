import React from 'react';
import PropTypes from 'prop-types';

class ClaimsTable extends React.Component {

  constructor() {
    super();
    this.state = {
      a: 0,
    };
  }

  render() {
    return (
      <div>
        hello claims table:
        {this.props.user}
      </div>
    );
  }
}

ClaimsTable.propTypes = {
  user: PropTypes.array.isRequired,
};

export default ClaimsTable;
