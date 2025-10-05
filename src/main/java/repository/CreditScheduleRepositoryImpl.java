package repository;

import domain.CreditSchedule;
import repository.Interface.CreditScheduleRepository;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreditScheduleRepositoryImpl implements CreditScheduleRepository {
    @Override
    public void save(CreditSchedule creditSchedule) {
        String sql =  "INSERT INTO credit_schedule (credit_account_id,due_date,principal,interest,total_payment,remaininf_balance)" +
                "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,creditSchedule.getCreditAccount().getId());
            stmt.setObject(2,creditSchedule.getDueDate());
            stmt.setBigDecimal(3,creditSchedule.getPrincipal());
            stmt.setBigDecimal(4,creditSchedule.getInterest());
            stmt.setBigDecimal(5,creditSchedule.getTotalPayment());
            stmt.setBigDecimal(6,creditSchedule.getRemainingBalance());

            stmt.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
