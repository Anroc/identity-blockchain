import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import PermissionTable from './PermissionTable';

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
        <PermissionTable permissions={this.props.permissions} />
      </section>
    );
  }
}

PermissionsSection.propTypes = {
  getPermissionRequest: PropTypes.func,
  permissions: PropTypes.array,
};

export default PermissionsSection;
