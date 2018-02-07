import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import MessagesTable from './MessagesTable';

class MessageSection extends React.Component {
  componentDidMount() {
  }

  render() {
    return (
      <section>
        <div>
          <Button
            onClick={this.props.getMessages}
          >
            Get Messages
          </Button>
        </div>
        <div>
          <MessagesTable messages={this.props.messages} />
        </div>
      </section>
    );
  }
}

MessageSection.propTypes = {
  getMessages: PropTypes.func,
  messages: PropTypes.array,
};

export default MessageSection;
