import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Card, CardBody, CardTitle, CardText } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  return (
    <Row>
      <Col md="12">
        <h1 className="display-4 mb-4">Hello, Guest!</h1>

        <Card className="mb-4 shadow-sm">
          <CardBody>
            <CardTitle tag="h5" className="mb-3">
              <strong>📌 과금 기준</strong>
            </CardTitle>
            <CardText>
              <ul style={{ listStyleType: 'none', paddingLeft: 0, lineHeight: '2' }}>
                <li>
                  👥 20인 이하 사업장: <strong>2K</strong>
                </li>
                <li>
                  👥 30인 이하 사업장: <strong>3K</strong>
                </li>
                <li>
                  👥 40인 이하 사업장: <strong>4K</strong>
                </li>
                <li>
                  👥 50인 이하 사업장: <strong>5K</strong>
                </li>
                <li>
                  👥 100인 이하 사업장: <strong>10K</strong>
                </li>
              </ul>
            </CardText>
          </CardBody>
        </Card>

        <Row>
          <Col md="6">
            <div className="ratio ratio-16x9">
              <iframe src="https://www.youtube.com/embed/dQw4w9WgXcQ" title="YouTube video" allowFullScreen></iframe>
            </div>
          </Col>
          <Col md="6">
            <div className="ratio ratio-16x9">
              <iframe src="https://www.youtube.com/embed/dQw4w9WgXcQ" title="YouTube video" allowFullScreen></iframe>
            </div>
          </Col>
        </Row>
      </Col>
    </Row>
  );
};

export default Home;
