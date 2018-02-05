import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';

class PermissionsSection extends React.Component {
  componentDidMount() {
  }
  render() {
    return (
      <section>
        <Button
          onClick={this.props.getPermissionRequest}
        >
          Get Permissions mit id von message
        </Button>

      </section>
    );
  }
}

PermissionsSection.propTypes = {
  getPermissionRequest: PropTypes.func,
};

export default PermissionsSection;
