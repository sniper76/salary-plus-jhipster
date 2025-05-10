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
        <p className="title">
          바 마사지 레스토랑 등의 사업장 근무자의 셀러리를 관리하기 위한 프로토 타입 사이트입니다. 2025년 6월까지 시범 운영 후 수요가 없을시
          서비스를 종료합니다.
        </p>
        <br />
        <p className="title">테스트 계정</p>
        <ul>
          <li>근무,주문 가능 계정 user / user</li>
          <li>매니저 계정 manager / manager</li>
          <li>투자자 계정 investor / investor</li>
        </ul>
        <Card className="mb-4 shadow-sm">
          <CardBody>
            <CardTitle tag="h5" className="mb-3">
              <strong>📌 예상 과금 기준</strong>
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
          <Col md="4">
            <div className="ratio ratio-16x9">
              <iframe src="https://www.youtube.com/embed/80tm6dL4xNo" title="YouTube video" allowFullScreen></iframe>
            </div>
          </Col>
          <Col md="4">
            <div className="ratio ratio-16x9">
              <iframe src="https://www.youtube.com/embed/gML4kGI1vTU" title="YouTube video" allowFullScreen></iframe>
            </div>
          </Col>
          <Col md="4">
            <div className="ratio ratio-16x9">
              <iframe src="https://www.youtube.com/embed/f3aZdWEElfk" title="YouTube video" allowFullScreen></iframe>
            </div>
          </Col>
        </Row>
      </Col>
    </Row>
  );
};

export default Home;
