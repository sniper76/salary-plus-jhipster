import React, { useState } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import { languages, locales } from 'app/config/translation';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';

export interface IOrderModalProps {
  showModal: boolean;
  orderError: boolean;
  handleOrder: (username: string, password: string, rememberMe: boolean) => void;
  handleClose: () => void;
  salesItems: ReadonlyArray<IShopSalesItem>;
  models: [];
  orderId: number;
}

const OrderModal = (props: IOrderModalProps) => {
  const order = ({ username, password, rememberMe }) => {
    props.handleOrder(username, password, rememberMe);
  };
  console.warn('props.salesItems', props.salesItems);
  console.warn('props.models', props.models);
  console.warn('props.orderId', props.orderId);

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const { orderError, handleClose } = props;

  const handleOrderSubmit = e => {
    handleSubmit(order)(e);
  };

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [rightItems] = useState([
    { type: true, name: 'Long Time' },
    { type: true, name: 'Short Time' },
    { type: false, name: 'Lady Drink' },
    { type: false, name: 'Guest Drink' },
  ]); // 오른쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  const handleDragStart = item => {
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handleRemove = index => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
  };

  return (
    <Modal isOpen={props.showModal} toggle={handleClose} backdrop="static" id="order-page" autoFocus={false}>
      <Form onSubmit={handleOrderSubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleClose}>
          <Translate contentKey="global.menu.order">Order</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="container">
                {/* 왼쪽 Div (드롭 가능) */}
                <div id="left" className="box" onDragOver={handleDragOver} onDrop={handleDrop}>
                  {leftItems.map((item, index) => (
                    <div key={index} className="item">
                      <div className="item-header">
                        <span className="item-name">{item.nameKo}</span>
                        <button className="close-btn" onClick={() => handleRemove(index)}>
                          ✖
                        </button>
                      </div>
                      {item.type && (
                        <ValidatedField className="select-box" type="select" id="langKey" name="langKey" data-cy="langKey">
                          {locales.map(locale => (
                            <option value={locale} key={locale}>
                              {languages[locale].name}
                            </option>
                          ))}
                        </ValidatedField>
                      )}
                    </div>
                  ))}
                </div>

                {/* 오른쪽 Div (드래그 가능) */}
                <div id="right" className="box">
                  {props.salesItems.map((item, index) => (
                    <div key={index} className="item" draggable onDragStart={() => handleDragStart(item)}>
                      {item.nameKo}
                    </div>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>{' '}
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="login.form.button">Sign in</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModal;
