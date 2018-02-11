import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';

class Anfragen extends React.Component {
  render() {
    return (
      <Paper className="">
        {this.props.permissions.map((permission) => (
          <div key={permission.id}>
            <div>
              <p>
                {permission.requiredClaims && JSON.stringify(permission.requiredClaims)}
              </p>
              <p>
                {permission.optionalClaims && JSON.stringify(permission.optionalClaims)}
              </p>
            </div>
            <div>
              Closures:
              {permission.closureRequestDTO && permission.closureRequestDTO.map((dto) => (
                <div>
                  Description:
                  {dto.description ? dto.description : null}
                </div>
              ))}
            </div>
          </div>
          ))}
      </Paper>
    );
  }
}

Anfragen.propTypes = {
  permissions: PropTypes.array,
};

export default Anfragen;
