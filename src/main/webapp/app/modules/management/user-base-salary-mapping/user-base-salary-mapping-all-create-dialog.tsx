import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createMappingAllUsers } from './user-base-salary-mapping.reducer';
import { getShopBaseSalaries } from 'app/modules/management/base-salary/base-salary.reducer';

export const UserBaseSalaryMappingAllCreateDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const { shopId } = useParams<'shopId'>();

  const [selectedValue, setSelectedValue] = useState(-1);
  const baseSalariesForPage = useAppSelector(state => state.baseSalaries.baseSalariesForPage);

  useEffect(() => {
    dispatch(getShopBaseSalaries({ shopId }));
  }, []);

  useEffect(() => {
    if (baseSalariesForPage.length > 0) {
      setSelectedValue(baseSalariesForPage[0].id);
    }
  }, [baseSalariesForPage]);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/user-base-salary-mapping');
  };

  const handleSelect = event => {
    setSelectedValue(event.target.value);
  };

  const confirmSave = event => {
    const entity = {
      shopBaseSalaryId: selectedValue,
      shopId,
    };
    dispatch(createMappingAllUsers(entity));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="userBaseSalaryMapping.home.allMappingLabel">전체 사용자 일당 연결하기</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="userBaseSalaryMapping.questions.allMapping">전체 사용자의 기본 일당을 연결하시겠습니까?</Translate>
        <ValidatedField type="select" name="shopBaseSalaryId" defaultValue={selectedValue} onChange={handleSelect}>
          {baseSalariesForPage.map(shop => (
            <option value={shop.id} key={shop.id}>
              {shop.nameKo}
            </option>
          ))}
        </ValidatedField>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">취소</Translate>
        </Button>
        <Button color="danger" onClick={confirmSave}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.save">저장</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default UserBaseSalaryMappingAllCreateDialog;
