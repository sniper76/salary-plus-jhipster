import React, { useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { Badge, Button, Row, Col } from 'reactstrap';
import { TextFormat, Translate, translate, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopOnlyModels, getModel, updateUserMamaMappings } from './model-management.reducer';

export const ModelManagementMamaMapping = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { shopId, userId } = useParams();

  useEffect(() => {
    dispatch(getModel({ shopId, userId }));
    dispatch(getShopOnlyModels({ shopId, userId }));
  }, []);

  const user = useAppSelector(state => state.modelManagement.user);
  const onlyModelUsers = useAppSelector(state => state.modelManagement.onlyModelUsers);

  // ✅ 체크박스 상태를 관리하는 useState
  const [selectedMappings, setSelectedMappings] = useState([]);

  // ✅ 조회된 onlyModelUsers 데이터를 기반으로 체크박스 초기값 설정
  useEffect(() => {
    if (onlyModelUsers.length > 0) {
      // const array = onlyModelUsers.filter(item => item.id === item.targetUserId).map(item => item.id);
      setSelectedMappings(onlyModelUsers.filter(item => item.id === item.targetUserId).map(item => item.id));
      console.warn('onlyModelUsers', onlyModelUsers, selectedMappings);
    }
  }, [onlyModelUsers]);

  // ✅ 체크박스 변경 시 상태 업데이트
  const handleCheckboxChange = (modelUserId, checked) => {
    setSelectedMappings(
      prevMappings =>
        checked
          ? [...prevMappings, modelUserId] // 선택된 항목 추가
          : prevMappings.filter(id => id !== modelUserId), // 선택 해제 시 제거
    );
  };

  // ✅ "저장" 버튼 클릭 시 선택된 데이터 서버로 전송
  const handleSave = () => {
    dispatch(updateUserMamaMappings({ shopId, userId, modelUserIds: selectedMappings })).then(() => {
      dispatch(getShopOnlyModels({ shopId, userId }));
    });
  };

  return (
    <div>
      <h2>
        <Translate contentKey="userManagement.detail.title">User</Translate> [<strong>{user.login}</strong>]
      </h2>
      <Row size="md">
        {onlyModelUsers.map((item, idx) => (
          <Col key={idx} xs="auto" className="border text-center p-3">
            <input
              type="checkbox"
              name={`hasMama_${item.id}`}
              className="form-check-input"
              value={item.id}
              checked={selectedMappings.includes(item.id)}
              onChange={e => handleCheckboxChange(item.id, e.target.checked)}
            />
            <p>{item.modelNo}</p>
            <p>
              {item.lastName} {item.firstName}
            </p>
          </Col>
        ))}
      </Row>
      <Row size="md" className="d-none d-md-inline">
        <Col md={12}>
          <Button onClick={() => navigate('/shop/model-management')} color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">뒤로</Translate>
            </span>
          </Button>
          <Button onClick={handleSave} color="primary">
            <FontAwesomeIcon icon="save" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.save">저장</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    </div>
  );
};

export default ModelManagementMamaMapping;
