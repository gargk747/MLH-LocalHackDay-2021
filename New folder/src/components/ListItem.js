import React, { useState } from 'react';
import { Col, Row, Form } from "react-bootstrap"
import "./ListItem.css"

function ListItem(props) {
    const [isComplete, setIsComplete] = useState(false)

    let styles = {}
    if(isComplete) {
        styles = {backgroundColor: "#f8a5c2"}
    }

  return (
    <div className="list-item" style={styles}>
        <Row>
            <Col xs={1}>
                <Form.Check
                    type="checkbox"
                    value={isComplete}
                    onChange={ () => {setIsComplete(!isComplete)} }
                    />
            </Col>
            <Col xs={11}>{props.item}</Col>
        </Row>
    </div>
  );
}

export default ListItem;
