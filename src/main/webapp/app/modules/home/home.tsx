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
          서비스를 종료합니다. 사이트 컨셉에 관심이 있으신 분들은 아래 메일로 문의 주시면 친절히 안내해 드리겠습니다.
        </p>
        <Link className="title" style={{ color: 'white' }} to="mailto:salaryplus2025@gmail.com">
          salaryplus2025@gmail.com
        </Link>
        <p></p>
        <p className="title">테스트 계정</p>
        <ul>
          <li>근무,주문 가능 계정 user / user</li>
          <li>매니저 계정 manager / manager</li>
          <li>투자자 계정 investor / investor</li>
        </ul>
        <p></p>
        <ul>
          <li>
            <a
              style={{ color: 'white', fontSize: '1.2em' }}
              href="https://www.youtube.com/watch?v=80tm6dL4xNo"
              target="_blank"
              rel="noopener noreferrer"
            >
              근무 주문 관리 설명 유튜브 보기
            </a>
          </li>
          <li>
            <a
              style={{ color: 'white', fontSize: '1.2em' }}
              href="https://www.youtube.com/watch?v=gML4kGI1vTU"
              target="_blank"
              rel="noopener noreferrer"
            >
              매니저 관리자 설명 유튜브 보기
            </a>
          </li>
          <li>
            <a
              style={{ color: 'white', fontSize: '1.2em' }}
              href="https://www.youtube.com/watch?v=f3aZdWEElfk"
              target="_blank"
              rel="noopener noreferrer"
            >
              대표 투자자 설명 유튜브 보기
            </a>
          </li>
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
      </Col>
    </Row>
  );
};

export default Home;
