import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { useAppDispatch } from 'app/config/store';
import { deleteOrderDetail } from 'app/modules/order/order.reducer';
import { IOrderDetail } from 'app/shared/model/orderDetail.model';

export interface IOrderModalDiscountProps {
  showPayModal: boolean;
  orderError: boolean;
  handlePayOrder: (obj: any) => void;
  handlePayClose: () => void;
  orderDetails: IOrderDetail[];
  orderDetailWithDiscounts: [];
  orderId: null;
}

const OrderModalDiscount = (props: IOrderModalDiscountProps) => {
  const orderHandlePayProps = obj => {
    props.handlePayOrder(obj);
  };

  const {
    handleSubmit,
    register,
    reset,
    watch,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const dispatch = useAppDispatch();

  const { orderError, handlePayClose } = props;

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  const handleOrderPaySubmit = handleSubmit(data => {
    orderHandlePayProps(data);
    handlePayClose();
  });

  const handleDragStart = item => {
    // console.warn('handleDragStart', item);
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      reset();
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handlePayRemove = (index, item) => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
    // console.warn('handlePayRemove', item);
    if (item.salesItemId) {
      dispatch(deleteOrderDetail({ orderId: props.orderId, orderDetailId: item.id }));
    }
  };

  useEffect(() => {
    reset(); // react-hook-form 의 상태도 초기화
    if (props.orderId && props.orderDetailWithDiscounts && props.orderDetailWithDiscounts.length > 0) {
      // console.warn('OrderModalDiscount: 기존 주문 불러오기', props.orderDetailWithDiscounts);
      setLeftItems([...props.orderDetailWithDiscounts]); // 기존 주문 정보로 leftItems 설정
    } else if (!props.orderId) {
      // console.warn('OrderModalDiscount: 새로운 주문 초기화');
      setLeftItems([]); // 새로운 주문일 경우 leftItems 초기화
    }
  }, [props.orderDetailWithDiscounts, props.orderId, reset]);

  // leftItems 값이 실제로 업데이트된 이후 확인
  useEffect(() => {
    // console.warn('OrderModalDiscount leftItems Updated', leftItems);
  }, [props]);

  // watch() 는 폼의 모든 값을 실시간으로 관찰합니다.
  const allValues = watch();

  useEffect(() => {
    // console.warn('Current Form Values:', allValues); // 값이 변경될 때마다 출력
  }, [allValues]);

  return (
    <Modal
      isOpen={props.showPayModal}
      toggle={handlePayClose}
      backdrop="static"
      id="order-discount-page"
      autoFocus={false}
      modalClassName="custom-discount-modal"
    >
      <Form onSubmit={handleOrderPaySubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handlePayClose}>
          <Translate contentKey="global.menu.order">Order</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="container-discount">{props.orderId}</div>
              <div className="container-discount">
                <div className="box">
                  {props.orderDetails &&
                    props.orderDetails.map((item, index) => {
                      return (
                        <div key={index} className="item">
                          <div className="item-header">
                            <p>{item.nameKo}</p>
                            <p>{item.price}</p>
                          </div>
                        </div>
                      );
                    })}
                </div>

                {/* 왼쪽 Div (드롭 가능) */}
                <div id="left" className="box" onDragOver={handleDragOver} onDrop={handleDrop}>
                  {leftItems.map((item, index) => {
                    // console.warn('Rendering item:', item, leftItems); // 콘솔 출력
                    return (
                      <div key={index} className="item">
                        <div className="item-header">
                          <div className="top">
                            {item.nameKo}
                            <ValidatedField
                              type="hidden"
                              register={register}
                              id={`salesItemIds[${index}]`}
                              name={`salesItemIds[${index}]`}
                              data-cy={`salesItemIds[${index}]`}
                              value={item.id}
                            />
                            <ValidatedField
                              type="hidden"
                              register={register}
                              id={`orderDetailIds[${index}]`}
                              name={`orderDetailIds[${index}]`}
                              data-cy={`orderDetailIds[${index}]`}
                              value={item.orderDetailId}
                            />
                            <button className="close-btn" onClick={() => handlePayRemove(index, item)}>
                              ✖
                            </button>
                          </div>
                          <div className="bottom">
                            {errors?.prices && (
                              <div>
                                <Alert color="warning" fade={false}>
                                  <Translate contentKey="error.order.empty.price">Price cannot be empty.</Translate>
                                </Alert>
                              </div>
                            )}
                            {item.snackYn ? (
                              <ValidatedField
                                register={register}
                                type="number"
                                id={`prices[${index}]`}
                                name={`prices[${index}]`}
                                validate={{
                                  required: { value: true, message: 'Price is required.' },
                                  min: { value: 1, message: 'Price must be a positive number.' },
                                }}
                                data-cy={`prices[${index}]`}
                                defaultValue={item.price || 0}
                              />
                            ) : (
                              <ValidatedField
                                register={register}
                                type="hidden"
                                id={`prices[${index}]`}
                                name={`prices[${index}]`}
                                data-cy={`prices[${index}]`}
                                value={item.price || 0}
                              />
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>

                {/* 오른쪽 Div (드래그 가능) */}
                <div id="right" className="box">
                  {props.orderDetailWithDiscounts.map((item, index) => (
                    <div key={index} className="item" draggable onDragStart={() => handleDragStart(item)}>
                      {item}
                    </div>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handlePayClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>{' '}
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.button.pay_processing">Payment Processing</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModalDiscount;
