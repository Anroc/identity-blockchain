import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';

import LazyImage from '../common/LazyImage';

class QRCode extends React.Component {
  componentDidMount() {
    this.props.showQRCode.bind(this);
  }
  render() {
    return (
      <div>
        <Button raised color="primary" onClick={this.props.showQRCode}>
          Show QR Code
        </Button>
        {this.props.showQR ?
          <p>
            QR Code:
            <LazyImage
              src="http://srv01.snet.tu-berlin.de:1112/account/qr-code"
              alt="qr-code"
            />
          </p> : null}
      </div>
    );
  }
}

QRCode.propTypes = {
  showQRCode: PropTypes.func,
  showQR: PropTypes.bool,
};


export default QRCode;
