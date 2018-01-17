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
        <p>
        Here is your claims table:
        {JSON.stringify(this.props.user)}
        </p>
      </div>
    );
  }
}

ClaimsTable.propTypes = {
  user: PropTypes.array,
};

export default ClaimsTable;
