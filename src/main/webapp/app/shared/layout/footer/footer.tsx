import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12 text-center">
        <p>
          <Translate contentKey="footer">Your footer</Translate>
        </p>
        <p>Copyright 2025. salaryplus2025@gmail.com All Rights Reserved.</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
