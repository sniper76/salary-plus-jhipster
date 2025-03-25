import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteSalesItemDiscount, getShopSalesItemDiscount } from './sales-item-discount.reducer';

export const SalesItemDiscountDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, salesItemDiscountId } = useParams();

  useEffect(() => {
    dispatch(getShopSalesItemDiscount({ shopId, salesItemDiscountId }));
  }, []);

  const salesItemDiscount = useAppSelector(state => state.salesItemDiscounts.salesItemDiscount);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/sales-item');
  };

  const confirmDelete = event => {
    dispatch(deleteSalesItemDiscount({ shopId, salesItemDiscountId }));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="salesItemDiscount.delete.question" interpolate={{ name: salesItemDiscount.nameKo }}>
          정말로 상품를 삭제하시겠습니까?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SalesItemDiscountDeleteDialog;
