import React, { useEffect, useState } from 'react';
import { Col, Container, Row } from 'reactstrap';
import './work.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserRoles } from 'app/modules/work/work.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBath, faBed, faWineGlass } from '@fortawesome/free-solid-svg-icons';

export const Work = () => {
  const account = useAppSelector(state => state.authentication.account);
  const work = useAppSelector(state => state.works.work);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getUserRoles());
  }, []);

  const btnOnClick = () => {
    console.error('Work', work, account);
  };
  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);

  const [products, setProducts] = useState([
    { name: 'A', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'B', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'C', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'D', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'E', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'F', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'G', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
    { name: 'H', longTimeValue: 0, shortTimeValue: 0, ladyDrinkValue: 0 },
  ]);

  const increaseLongTime = (index: number) => {
    const list = [...products] as any;
    list[index].longTimeValue += 1;
    setProducts(list);
  };

  const decreaseLongTime = (index: number) => {
    const list = [...products] as any;
    if (list[index].longTimeValue === 0) {
      list[index].longTimeValue = 0;
    } else {
      list[index].longTimeValue -= 1;
    }
    setProducts(list);
  };

  const increaseShortTime = (index: number) => {
    const list = [...products] as any;
    list[index].shortTimeValue += 1;
    setProducts(list);
  };

  const decreaseShortTime = (index: number) => {
    const list = [...products] as any;
    if (list[index].shortTimeValue === 0) {
      list[index].shortTimeValue = 0;
    } else {
      list[index].shortTimeValue -= 1;
    }
    setProducts(list);
  };

  const increaseLadyDrink = (index: number) => {
    const list = [...products] as any;
    list[index].ladyDrinkValue += 1;
    setProducts(list);
  };

  const decreaseLadyDrink = (index: number) => {
    const list = [...products] as any;
    if (list[index].ladyDrinkValue === 0) {
      list[index].ladyDrinkValue = 0;
    } else {
      list[index].ladyDrinkValue -= 1;
    }
    setProducts(list);
  };

  return (
    <Container>
      <div className="custom-date-input">
        <input defaultValue={currentDate} type="date" />
      </div>
      <Row>
        {products.map((item, idx) => (
          <Col key={idx} xs="auto" className="border text-center p-3">
            {item.name}사용자
            <div className="custom-number-input">
              <p className="icon-style">
                <FontAwesomeIcon icon={faBed} />
              </p>
              <button onClick={() => decreaseLongTime(idx)}>-</button>
              <input type="number" min="0" max="30" value={item.longTimeValue} readOnly={true} />
              <button onClick={() => increaseLongTime(idx)}>+</button>
            </div>
            <div className="custom-number-input">
              <p className="icon-style-bath">
                <FontAwesomeIcon icon={faBath} />
              </p>
              <button onClick={() => decreaseShortTime(idx)}>-</button>
              <input type="number" min="0" max="30" value={item.shortTimeValue} readOnly={true} />
              <button onClick={() => increaseShortTime(idx)}>+</button>
            </div>
            <div className="custom-number-input">
              <p className="icon-style-wine">
                <FontAwesomeIcon icon={faWineGlass} />
              </p>
              <button onClick={() => decreaseLadyDrink(idx)}>-</button>
              <input type="number" min="0" max="30" value={item.ladyDrinkValue} readOnly={true} />
              <button onClick={() => increaseLadyDrink(idx)}>+</button>
            </div>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default Work;
